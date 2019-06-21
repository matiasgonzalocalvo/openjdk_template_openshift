def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  //env.sonar_projectKey="comafi-common-account-service"
  env.ECR_ID="commons/account"
  env.APPNAME="commons-account"
  env.AWS_DEFAULT_REGION='us-east-1'
  devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    // no encontre en produccion el job analizar
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_QA_CMF")
    ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    ECR_ID="commons/account"
    env.maven_cobertura="true"
    env.maven_sonar="true"
    env.maven_release_prepare="true"
    env.maven_release_perform="true"
    env.reltag="true"
    env.docker_build_push_tag1="true"
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    /*env.ECR_ID="commons/account"
    env.APPNAME="commons-account"*/
    env.ENVNAME="dev"
    env.TAG1="${BUILD_NUMBER}"
    env.TAG2="latest"
    env.CLOUD_AWS_STACK_AUTO=false
    env.aws_key_2="ECSUpdateService"
    env.maven_verify="true"
    env.maven_sonar="true"
    env.maven_deploy="true"
    env.docker_build_push_tag1="true"
    env.docker_tag_latest_push="true"
    env.update_esc=true
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
    devops.config_file_provider("9bca46b5-370c-4e9c-97bf-447123727106")
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    env.ENVNAME="sandbox"
    env.TAG1="${BUILD_NUMBER}"
    env.TAG2="sandbox"
    env.CLOUD_AWS_STACK_AUTO="false"
    env.aws_key_2="ECSUpdateService"
    env.maven_verify="true"
    env.maven_sonar="true"
    env.maven_deploy="true"
    env.docker_build_push_tag1="true"
    env.docker_tag_latest_push="true"
    env.update_esc=true
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
