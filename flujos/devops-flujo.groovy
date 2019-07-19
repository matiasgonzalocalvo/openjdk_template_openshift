#!groovy
def flujo() 
{
  try
  {
    print node_docker
  }
  catch (e)
  {
    node_docker="master"
  }
  node ("${node_docker}")
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
            loadvar.setenv()
          }
        }
        stage("updateando la version")
        {
          devops.update_version()
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
          devops.postfinal()
        }
      }
    } 
  }
}
return this;
