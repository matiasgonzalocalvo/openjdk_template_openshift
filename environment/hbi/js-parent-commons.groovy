def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="js-parent-commons"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
  env.ECR_ID="commons/adhesion"
  env.APPNAME="commons-adhesion"
  env.node_docker="jenkins-slave-comafi-maven3.3.9"
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.maven_verify="true"
    env.maven_cobertura="true"
    env.maven_sonar="true"
    env.maven_deploy="true"
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.maven_verify="true"
    env.maven_cobertura="true"
    env.maven_sonar="true"
    env.maven_deploy="true"
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.maven_verify="true"
    env.maven_cobertura="true"
    env.maven_sonar="true"
    env.maven_deploy="true"
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.maven_verify="true"
    env.maven_cobertura="true"
    env.maven_sonar="true"
    env.maven_deploy="true"
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
