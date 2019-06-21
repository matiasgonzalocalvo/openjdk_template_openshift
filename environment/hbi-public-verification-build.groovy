def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.AWS_DEFAULT_REGION='us-east-1'
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
 
    //env.tag="true"

  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
   sh 'echo " Deploy a QA"
   // Ver tema aprobaciones
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
  //Ver tema de usar With Credentials
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENVNAME="dev"
    env.aws_key_1="705437fe-118c-4fbc-af26-595cbdc1e752"
    env.aws_key_2="ECSUpdateService"
    env.ECR_URL="104455529394.dkr.ecr.us-east-1.amazonaws.com"
    env.ECR_ID="public-verification"
    env.APPNAME="public-verification"
    env.TAG1="$BUILD_NUMBER"
    env.TAG2="latest"
    env.AWS_DEFAULT_REGION="us-east-1"
    env.CLOUD_AWS_STACK_AUTO="false"
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
