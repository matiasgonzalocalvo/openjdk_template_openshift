def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="comafi-hbe-adhesion-service"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
  env.ECR_ID="js/dummy-java"
  env.APPNAME="js-dummy-java"
  env.node_docker="jenkins-slave-comafi-maven3.3.9"
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    env.ECR_URL2="367760667466.dkr.ecr.us-east-1.amazonaws.com"
    env.ENVNAME="prod"
    env.TAG1="qa"
    env.TAG2="prod"
    env.docker_pull="true"
    env.docker_tag="true"
    env.docker_push_prod="true"
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    env.ENVNAME="glqa"
    env.TAG2="glqa"
    env.maven_cobertura="true"
    env.maven_sonar="true"
    env.maven_release_prepare="true"
    env.maven_release_perform="true"
    env.reltag="true"
    env.docker_tag1_tag2="true"
    env.docker_build_tag1="true"
    env.docker_push_tag1="true"
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.ENVNAME="dev"
    env.aws_key_1="705437fe-118c-4fbc-af26-595cbdc1e752"
    env.aws_key_2="ECSUpdateService"
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    env.TAG1="$BUILD_NUMBER"
    env.TAG2="latest"
    env.CLOUD_AWS_STACK_AUTO="false"
    env.aws_key_2="ECSUpdateService"
    env.maven_verify="true"
    env.maven_sonar="true"
    env.maven_deploy="true"
    env.docker_build_tag1="true"
    env.docker_push_tag1="true"
    env.docker_tag1_tag2="true"
    env.docker_push_tag2="true"
    env.change_aws_key="true"
    env.update_esc=true
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.ENVNAME="sandbox"
    env.aws_key_1="705437fe-118c-4fbc-af26-595cbdc1e752"
    env.aws_key_2="ECSUpdateService"
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    env.TAG1="$BUILD_NUMBER"
    env.TAG2="sandbox"
    env.CLOUD_AWS_STACK_AUTO="false"
    env.aws_key_2="ECSUpdateService"
    env.maven_verify="true"
    env.maven_cobertura="true"
    env.maven_sonar="true"
    env.maven_deploy="true"
    env.docker_build_tag1="true"
    env.docker_push_tag1="true"
    env.docker_tag1_tag2="true"
    env.docker_push_tag2="true"
    env.change_aws_key="true"
    env.update_esc=true
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
