#!groovy
def flujo() 
{
  pipeline 
  {
    try 
    {
      stage('set env')
      {
        try
        {
          loadvar.set_env_global() 
        }
        catch (e)
        {
          echo 'No existen variables Globales ' + e.toString()
        }
        loadvar.setenv()
        devops.docker_login()
      }
      stage('Start Redis')
      {
        devops.redis_start()
      }
      stage("maven verify")
      {
        devops.maven_verify("${settings}")
      }
      stage("maven sonar")
      {
        devops.maven_sonar("${settings}","http://172.19.130.26:9000","694e463e93ba0a27427fb8a46a266abc42c0f542","MatiasCalvo")
      }
      stage("maven deploy")
      {
        devops.maven_deploy("${settings}")
      }
      stage("Docker build image")
      {
        devops.docker_build("${ECR_URL}","${ECR_ID}","${TAG1}","tcp://${JENKINS_IP}:2376")
      }
      stage("Push image")
      {
        devops.docker_push("${ECR_URL}","${ECR_ID}","${TAG1}","tcp://${JENKINS_IP}:2376")
      }
      stage("Tag image")
      {
        devops.docker_tag("${ECR_URL}","${ECR_ID}","${TAG1}","${TAG2}","tcp://${JENKINS_IP}:2376")
      }
      stage("Push image 2 ")
      {
        devops.docker_push("${ECR_URL}","${ECR_ID}","${TAG2}","tcp://${JENKINS_IP}:2376")
      }
      stage("change aws key")
      {
        devops.aws_config("${aws_key_2}")
      }
      stage("Update ecs service")
      {
        deploy.ecs_update_service("${ENVNAME}-cluster", "${ENVNAME}-${APPNAME}-service", "${ENVNAME}-${APPNAME}-task")
      }
      stage("Tag Image to sandbox ")
      {
        devops.docker_tag("${ECR_URL}","${ECR_ID}","${TAG1}","sandbox","tcp://${JENKINS_IP}:2376")
      }
      stage("Push image sandbox")
      {
        devops.docker_push("${ECR_URL}","${ECR_ID}","sandbox","tcp://${JENKINS_IP}:2376")
      }
      stage("Update ecs service 2")
      {
        deploy.ecs_update_service("sandbox-cluster", "sandbox-${APPNAME}-service", "sandbox-${APPNAME}-task")
      }
      stage("Pull Image sandbox")
      {
        devops.docker_pull("${ECR_URL}","${ECR_ID}","sandbox","tcp://${JENKINS_IP}:2376")
      }
    }
    catch (e) 
    {
      echo e.getMessage()
      echo 'Err: Build failed with Error: ' + e.toString()
      echo "FALLO !!!!!"
      devops.fail()
    }
    finally 
    {
      stage('Reportes')
      {
        devops.reporting()
        devops.postfinal()
      }
    }
  }
}
return this;
