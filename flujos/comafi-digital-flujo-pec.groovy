#!groovy
def flujo() 
{
  pipeline 
  {
    try 
    {
      //def cuentas=["AWS_DESA_CMF", "AWS_DESA","_srv_jenkins_pec"]
      def cuentas=["AWS_DESA_CMF", "AWS_DESA", "AWS_Alternative"]
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
      /*stage('test') 
      {
        devops.test_npm_comafi_digital()
      }*/
      stage('SonarQube analysis') 
      {
        devops.sonar_js("${sonar_projectKey}", "${sonar_exclusions}", "${sonar_javascript_lcov_reportPaths}")
      }
      stage("Quality Gate")
      {
        sh "echo sonar "
        //devops.wait_sonar()
      }
      for (cuenta in cuentas)
      {
        if ( loadvar.setenv(cuenta) ) 
        {
          stage("Build Comafi Digital ${cuenta}")
          {
            devops.build_comafi_digital()
          }
          stage("Deploy Comafi Digital ${cuenta}")
          {
            devops.deploy_comafi_digital()
          }
        }
        else
        {
          echo "No se hace nada en la cuenta ${cuenta}"
        }
      }
      if ( "${env.tag}" == "true" )
      {
        stage("Tag Comafi Digital")
        {
          devops.git_tag()
        }
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
