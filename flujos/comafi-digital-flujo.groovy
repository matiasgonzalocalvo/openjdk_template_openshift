#!groovy
def flujo() 
{
  pipeline 
  {
    try 
    {
      stage('set env')
      {
        loadvar.setenv()
      }
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
      stage("Build Comafi Digital")
      {
        devops.build_comafi_digital()
      }
      stage("Tag Comafi Digital")
      {
        if ( "${env.tag}" == "true" )
        {
          devops.git_tag()
        }
        else
        {
          echo "no se tagea || tag == ${env.tag} ||"
        }
      }
      stage("Deploy Comafi Digital")
      {
        devops.deploy_comafi_digital()
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
