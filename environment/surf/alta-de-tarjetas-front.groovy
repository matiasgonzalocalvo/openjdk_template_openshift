def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="alta-de-tarjetas-front"
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
    env.DOMAIN=""
    def verCode = UUID.randomUUID().toString()
    env.random="${verCode}"
    env.BUCKET="${ENV}-comafi-base-deploy"    
    env.STACK="${ENV}-INFRA-ADTC-FRONT"
    env.PROJECT="ALTA-DE-TARJETAS"
    env.DEFAULT_BUCKET="${ENV}-${ENV}-comercios-deploy-${ENV}"
    env.CERT_ARN=""
}
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_Alternative")
    env.ENV='qa'
    env.DOMAIN=""
    def verCode = UUID.randomUUID().toString()
    env.random="${verCode}"
    env.BUCKET="${ENV}-comafi-base-deploy"    
    env.STACK="${ENV}-INFRA-ADTC-FRONT"
    env.PROJECT="ALTA-DE-TARJETAS"
    env.DEFAULT_BUCKET="${ENV}-${ENV}-comercios-deploy-${ENV}"
    env.CERT_ARN=""
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_Alternative")
    env.ENV='dev'
    env.DOMAIN=""
    def verCode = UUID.randomUUID().toString()
    env.random="${verCode}"
    env.BUCKET="${ENV}-comafi-base-deploy"    
    env.STACK="${ENV}-INFRA-ADTC-FRONT"
    env.PROJECT="ALTA-DE-TARJETAS"
    env.DEFAULT_BUCKET="${ENV}-${ENV}-comercios-deploy-${ENV}"
    env.CERT_ARN=""
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_Alternative")
    env.ENV='dtkpredev'
    def verCode = UUID.randomUUID().toString()
    env.random="${verCode}"
    env.DOMAIN='comercios.co'
    env.BUCKET="${ENV}-comafi-base-deploy"    
    env.STACK="${ENV}-INFRA-ADTC-FRONT"
    env.PROJECT="ALTA-DE-TARJETAS"
    env.DEFAULT_BUCKET="${ENV}-${ENV}-comercios-deploy-${ENV}"
    env.CERT_ARN="arn:aws:acm:us-east-1:104455529394:certificate/a35d91f8-2b7f-4833-8af6-e2f5f5e54c23"
    env.PARAMETER_OVERRIDES="Environment=${ENV} DeployBucket=${BUCKET} HostedZoneName=${DOMAIN} CertArn=$CERT_ARN StackName=${STACK}"
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
