#!groovy
def flujo() 
{
  /*
    Genero Variables Globales que se van a utilizar
  */
  //node_docker="master"
  //node_docker="jenkins-slave-comafi-nodejsdtk"
  node ("master")
  {
    pipeline
    {
      stage('test docker')
      {
        sh '''
          #!/bin/bash
          df -h
          if [ $(docker ps -a | wc -l) -gt 0 ] ; then
            docker rm -f $(docker ps -a|awk '{print $1}'|grep -v CONTAINER)
          fi
          if [ $(docker images | wc -l) -gt 0 ] ; then
            docker rmi -f $(docker images|awk '{print $3}'|grep -v IMAGE)
          fi
          df -h 
        '''
      }
    }
  }
  //node_docker="jenkins-slave-comafi-maven3.3.9-redis"
  node_docker="arch-jenkins-slave-centos"
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
        stage("docker create repository")
        {
          devops.create_repository("${ECR_ID}")
        } 
        stage('Docker Build')
        {
          devops.docker_build("${ECR_URL}","${ECR_ID}","${TAG1}","tcp://${JENKINS_IP}:2376")
        }
        stage('Docker Push')
        {
          devops.docker_push("${ECR_URL}","${ECR_ID}","${TAG1}","tcp://${JENKINS_IP}:2376")
        }
        stage('borrar imagen local')
        {
          devops.docker_delete("${ECR_URL}","${ECR_ID}","${TAG1}","tcp://${JENKINS_IP}:2376")
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
