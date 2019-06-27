#!groovy
def flujo() 
{
  try
  {
    loadvar.set_env_global()
    print node_docker 
  }
  catch (e)
  {
    print e.getMessage()
    print 'Err:  ' + e.toString()
    echo "node_docker no existe seteo jenkins-slave-comafi-nodejsdtk"
    node_docker="jenkins-slave-comafi-nodejsdtk"
  }
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
          //devops.docker_login()
        }
        stage("Build SURF SAM Project")
        {
          //sh "export"
          devops.sam_package()
        }
        stage("Deploy SURF SAM Project")
        {
          devops.sam_deploy("${PARAMETER_OVERRIDE}")
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
        stage('Reportes')
        {
          devops.reporting()
          devops.postfinal()
        }
      }
    }
  }
}
return this;
