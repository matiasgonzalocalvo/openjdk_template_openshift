#!groovy
def flujo() 
{
  /*
    Genero Variables Globales que se van a utilizar
  */
  //node_docker="master"
  //node_docker="jenkins-slave-comafi-nodejsdtk"
  node_docker="jenkins-slave-comafi-maven3.3.9-redis"
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
          loadvar.setenv()
        }
        stage("Docker Login")
        {
          devops.docker_login()
        } 
        stage('Docker Build')
        {
          devops.devops.docker_build("${ECR_URL}","${ECR_ID}","${TAG1}","tcp://${JENKINS_IP}:2376")
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
