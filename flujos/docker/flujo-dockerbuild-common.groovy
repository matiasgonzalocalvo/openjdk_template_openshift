#!groovy
def flujo() 
{
  /*
    Genero Variables Globales que se van a utilizar
  */
  env.SOURCE_cloudformation="cloudformations"
  env.SOURCE_functions="functions"
  env.SOURCE_scripts="scripts"
  node_docker="master"
  node ("${node_docker}")
  {
    pipeline 
    {
      try 
      {
        stage('Checkout SCM')
        {
          checkout scm
        }
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
        } 
        stage('Docker Build')
        {
          devops.devops.docker_build("${ECR_URL}","${ECR_ID}","${TAG}","tcp://${JENKINS_IP}:2376")()
        }
        stage('Docker Push')
        {
          devops.docker_push("${ECR_URL}","${ECR_ID}","${TAG1}","tcp://${JENKINS_IP}:2376")
        }
      }
      catch (e) 
      {
        print e.getMessage()
        print 'Err: Build failed with Error: ' + e.toString()
        print "FALLO !!!!!"
        devops.fail()
      }
      finally 
      {
        devops.reporting()
        devops.postfinal()
      }
    }
  }
}
return this;
