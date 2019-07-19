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
        stage('Checkout SCM')
        {
          checkout scm
        }
        /*
        stage("updateando la version")
        {
          devops.update_version()
        }*/
        stage("test package determine_snapshot_release")
        {
          devops.determine_snapshot_release()
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
          echo "reportes"
          //devops.postfinal()
        }
      }
    } 
  }
}
return this;
