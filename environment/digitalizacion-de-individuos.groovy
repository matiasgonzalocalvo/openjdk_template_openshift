def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="digitalizacion-de-individuos"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
  //env.AWS_PROFILE="default"
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_Alternative")
    env.ENV='prod'
    devops.aws_config("AWS_DESA")
    env.random=Math.random() * 100
    env.BUCKET="comafi-base-deploy"
    env.FILES_BUCKET="dtkpredev-comercios-files-dtkpredev"
    env.STACK="${ENV}-INFRA-DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.PROJECT="DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENV='qa'
    devops.aws_config("AWS_DESA")
    env.random=Math.random() * 100
    env.BUCKET="comafi-base-deploy"
    env.FILES_BUCKET="dtkpredev-comercios-files-dtkpredev"
    env.STACK="${ENV}-INFRA-DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.PROJECT="DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENV='dev'
    devops.aws_config("AWS_DESA")
    env.random=Math.random() * 100
    env.BUCKET="comafi-base-deploy"
    env.FILES_BUCKET="dtkpredev-comercios-files-dtkpredev"
    env.STACK="${ENV}-INFRA-DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.PROJECT="DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENV='predev'
    devops.aws_config("AWS_DESA")
    env.random=Math.random() * 100
    env.BUCKET="comafi-base-deploy"
    env.FILES_BUCKET="dtkpredev-comercios-files-dtkpredev"
    env.STACK="${ENV}-INFRA-DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.PROJECT="DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
