def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.f_flujo="lambda/flujo-lambda-common.groovy"
  env.node_docker="arch-jenkins-slave-centos"
  env.AWS_DEFAULT_REGION='us-east-1'
    
    /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="${repoName}"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    return false
  }
  else if (env.BRANCH_NAME =~ "release/*" || env.BRANCH_NAME =~ "hotfix/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    return true
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    return false
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "bugfix/*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA_CMF")
    //devops.aws_config("AWS_DESA")
    env.ENV='predev'
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.STACK="${ENV}-INFRA-OPEN-API-CE-ATC"
    env.STACK_SECURITY="${ENV}-SECURITY-OPEN-API-CE-ATC"
    env.PROJECT="OPEN-API-CE-ATC"
    //# VARIABLES ESPECIFICAS
    env.URL_API='https://predev.desa-comafidigital.com/api'
    env.TOKEN_API='pendiente'    
    env.parameter_overrides="UUID=${random} Environment=${ENV} DeployBucket=${BUCKET} StackName=${STACK} UrlApi=${URL_API} TokenApi=${TOKEN_API}"
    env.parameter_overrides_security="Environment=${ENV}"
    return false
  }
  else
  {
    echo "ERROR: No entro a ninguna condicion de branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
