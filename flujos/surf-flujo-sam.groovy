#!groovy
def flujo() 
{
  pipeline 
  {
    try 
    {
      stage('set env')
      {
        loadvar.set_env_global() 
        loadvar.setenv()
      }
      stage("Build SURF SAM Project")
      {
        devops.sam_package($PARAMETER_OVERRIDE)
      }
      stage("Deploy SURF SAM Project")
      {
        devops.sam_deploy($PARAMETER_OVERRIDE)
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