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
    def verCode = UUID.randomUUID().toString()
    env.random="${verCode}"
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.FILES_BUCKET="${ENV}-lib-circuit-engine"
    env.STACK="${ENV}-INFRA-DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.PROJECT="DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.DEFAULT_BUCKET="${ENV}-comercios-files-${ENV}"
    env.ThubanHost="http://thuban-1886992369.us-east-1.elb.amazonaws.com"
    env.ThubanPassword="Thuban$2018"
    env.ThubanUser="DEI_USR"
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENV='qa'
    devops.aws_config("AWS_DESA")
    def verCode = UUID.randomUUID().toString()
    env.random="${verCode}"
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.FILES_BUCKET="${ENV}-lib-circuit-engine"
    env.STACK="${ENV}-INFRA-DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.PROJECT="DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.DEFAULT_BUCKET="${ENV}-comercios-files-${ENV}"
    env.ThubanHost="http://thuban-1902548003.us-east-1.elb.amazonaws.com:8080"
    env.ThubanPassword="thuban"
    env.ThubanUser="thuban_isladigital"
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENV='dtkdev'
    devops.aws_config("AWS_DESA")
    def verCode = UUID.randomUUID().toString()
    env.random="${verCode}"
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.FILES_BUCKET="${ENV}-lib-circuit-engine"
    env.STACK="${ENV}-INFRA-DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.PROJECT="DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.DEFAULT_BUCKET="${ENV}-comercios-files-${ENV}"
    env.ThubanHost="http://thuban-1902548003.us-east-1.elb.amazonaws.com:8080"
    env.ThubanPassword="thuban"
    env.ThubanUser="thuban_isladigital"
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENV='dtkpredev'
    devops.aws_config("AWS_DESA")
    //env.random=Math.random() * 100
    def verCode = UUID.randomUUID().toString()
    env.random="${verCode}"
    env.BUCKET="${ENV}-comafi-base-deploy"
    env.FILES_BUCKET="${ENV}-lib-circuit-engine"
    env.STACK="${ENV}-INFRA-DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.PROJECT="DIGITALIZACION-ELECTRONICA-INDIVIDUOS"
    env.DEFAULT_BUCKET="${ENV}-comercios-files-${ENV}"
    env.ThubanHost="http://thuban-1902548003.us-east-1.elb.amazonaws.com:8080"
    env.ThubanPassword="thuban"
    env.ThubanUser="thuban_isladigital"
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
