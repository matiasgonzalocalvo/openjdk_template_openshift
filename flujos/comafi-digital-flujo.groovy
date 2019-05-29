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
        try 
        {
          if (env.list_counts)
          {
            echo "existe env.list_count == ${env.list_counts}"
          }
        }
        catch (e)
        {
          echo "entro en catch"
          env.list_counts="null"
        }
        if ( env.list_counts == "null" )
        {
          echo "list es null | env.list_counts == ${env.list_counts} | | list_counts == ${list_counts} |"
        }
        else
        {
          for (counts in env.list_counts)
          {
            echo "for env cargando cuenta ${counts} | env.list_counts == ${env.list_counts}"
          }
          for (counts in list_counts)
          {
            echo "for sin env cargando cuenta ${counts} | env.list_counts == ${env.list_counts}"
          }
        }
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
      if ( "${env.tag}" == "true" )
      {
        stage("Tag Comafi Digital")
        {
          devops.git_tag()
        }
      }
      else
      {
        echo "no se tagea || tag == ${env.tag} ||"
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
