def flujo {
  pipeline 
  {
    agent 
    {
      label "${AGENT_LABEL}"
    }
    stages 
    {
      stage('set env')
      {
        steps 
        {
          script
          {
            //prepare_environment()
            setenv()
          }
        }
      }
      stage('test') 
      {
        steps 
        {
          script
          {
            echo "test"
            devops.test_npm()
          }
        }
      }
      stage('SonarQube analysis') 
      {
        steps
        {
          script
          {
            echo "sonar"
            devops.sonar_js("${sonar_projectKey}", "${sonar_exclusions}", "${sonar_javascript_lcov_reportPaths}")
          }
        }
      }
      stage("Quality Gate")
      {
        steps
        {
          script
          {
            sh "echo sonar "
            //sh "ping -c 1 sonarqube.developmentcomafi.com"
            //devops.wait_sonar()
          }
        }
      }
      stage("Build Comafi Digital")
      {
        steps
        {
          script
          {
            devops.build_comafi_digital()
            if ( "${env.tag}" == "true" ) 
            {
              devops.git_tag()
            }
            else
            {
              echo "no se tagea || tag == ${env.tag} ||"
            }
          }
        }
        }
      stage("Deploy Comafi Digital")
      {
        steps
        {
          script
          {
            devops.deploy_comafi_digital()
          }
        }
      }
    }
    post 
    {
      always 
      {
        script 
        {
          devops.send_slack("always",":squirrel:")
        }
      }
      failure 
      {
        script
        {
          devops.send_slack("failure",":squirrel:")
        }
      }
      unstable 
      {
        script
        {
          devops.send_slack("unstable",":squirrel:")
        }
      }
    }
  }
}
