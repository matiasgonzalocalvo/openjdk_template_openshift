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
      stage("Build New Circuit Engine")
      {
        new_process_sam()
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
