def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="hbi-auth-build"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comafi_digital_prod'
    //env.tag="true"
    if ( cuenta == "_srv_jenkins_pec" )
    {
      devops.aws_config("_srv_jenkins_pec")
      env.ENV='prod'
      env.STACK_NAME='CmfComerciosPortal'
      env.DOMAIN='comafidigital.com'
      env.CERT_ARN='arn:aws:acm:us-east-1:367760667466:certificate/3490adf8-e2f8-4c80-8083-9c6f2f123006'
      return true
    }
    else if ( cuenta == "AWS_Alternative" ) 
    {
      devops.aws_config("AWS_Alternative")
      env.ENV='prod'
      env.COST_CENTER='comafi_digital_prod'
      env.DOMAIN='comafidigital.com'
      env.CERT_ARN='arn:aws:acm:us-east-1:367760667466:certificate/3490adf8-e2f8-4c80-8083-9c6f2f123006'
      env.STACK_NAME='CMF-DIGITAL-PORTAL'
      return true
    }
    else
    {
      return false
    }
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comercios_dev'
    env.tag="true"
    if ( cuenta == "null" || cuenta == "AWS_QA_CMF" )
    {
      devops.aws_config("AWS_QA_CMF")
      env.ENV='qa'
      env.STACK_NAME='CmfComerciosPortal'
      env.DOMAIN='desa-comafidigital.com'
      env.CERT_ARN='arn:aws:acm:us-east-1:780666439417:certificate/6c9bb0c4-55d1-4026-afb2-3a3e72b9632c'
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      devops.aws_config("AWS_DESA")
      env.ENV='dtkqa'
      env.STACK_NAME='COMERCIOS-PORTAL'
      env.DOMAIN='comercios.co'
      env.CERT_ARN='arn:aws:acm:us-east-1:104455529394:certificate/a35d91f8-2b7f-4833-8af6-e2f5f5e54c23'
      return true
    }
    else
    {
      return false
    }
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENVNAME="dev"
    env.aws_key_1="705437fe-118c-4fbc-af26-595cbdc1e752"
    env.aws_key_2="ECSUpdateService"
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    env.ECR_ID="auth"
    env.APPNAME="auth"
    env.TAG1="$BUILD_NUMBER"
    env.TAG2="latest"
    env.AWS_DEFAULT_REGION="us-east-1"
    env.CLOUD_AWS_STACK_AUTO="false"
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENVNAME="dev"
    env.aws_key_1="705437fe-118c-4fbc-af26-595cbdc1e752"
    env.aws_key_2="ECSUpdateService"
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    env.ECR_ID="auth"
    env.APPNAME="auth"
    env.TAG1="$BUILD_NUMBER"
    env.TAG2="latest"
    env.AWS_DEFAULT_REGION="us-east-1"
    env.CLOUD_AWS_STACK_AUTO="false"
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
