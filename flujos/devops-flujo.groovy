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
          loadvar.setenv()
        }
      }
      stage("test devops flow")
      {
        echo "probando flow"
        sh "export"
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
