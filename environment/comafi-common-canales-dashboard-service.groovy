def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  //env.sonar_projectKey="comafi-common-account-service"
  env.ECR_ID="commons/account"
  env.APPNAME="commons-account"
  env.AWS_DEFAULT_REGION='us-east-1'
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("_srv_jenkins_pec")
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_QA_CMF")
    ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    ECR_ID="commons/account"
    env.RELTAG = sh(script: ''' git tag | xargs -I@ git log --format=format:"%ai @%n" -1 @ | sort | awk '{print $4}' | tail -n1 ''', returnStdout: true)
    sh """
      echo "TAG1=$RELTAG" > promotag
      git checkout $RELTAG
    """
    env.TAG1="${RELTAG}"
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    /*env.ECR_ID="commons/account"
    env.APPNAME="commons-account"*/
    env.ENVNAME="dev"
    env.TAG1="${BUILD_NUMBER}"
    env.TAG2="latest"
    env.CLOUD_AWS_STACK_AUTO=false
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
    aws1_key="705437fe-118c-4fbc-af26-595cbdc1e752"
    aws2_key="ECSUpdateService"
    ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    ECR_ID="commons/dashboard"
    APPNAME="commons-dashboard"
    ENVNAME="dev"
    TAG1="${BUILD_NUMBER}"
    TAG2="sandbox"
    CLOUD_AWS_STACK_AUTO="false"
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
