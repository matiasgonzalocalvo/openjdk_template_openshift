#!groovy
def flujo() 
{
  pipeline 
  {
    try 
    {
      def cuentas=["AWS_DESA_CMF", "AWS_DESA","_srv_jenkins_pec"]
      /*stage('set env')
      {
        //loadvar.setenv()
        for (cuenta in cuentas)
        {
          if ( loadvar.setenv(cuenta) )
          {
            echo "if ${cuenta}"
          }
          else
          {
            echo "else ${cuenta}"
          }
        }
      }*/
      stage('test') 
      {
        devops.test_npm_comafi_digital()
      }
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
      echo "FALLO !!!!!"
      devops.fail()
    }
    finally 
    {
      devops.postfinal()
    }
  }
}
return this;
