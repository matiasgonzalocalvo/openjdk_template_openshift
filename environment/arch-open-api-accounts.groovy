def setenv()
{
  /*
      Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="arch-open-api-accounts"
  env.sonar_exclusions="/test/**/*,/**/test/*,scripts/*,/**/build/*,**/.nyc_output/*,**/node_modules/*.test-reports/*,coverage/**/*,/**/coverage/**/*,/coverage/**/*,.coverage,**/scripts/*,/test/*/**"
  env.sonar_javascript_lcov_reportPaths="$WORKSPACE/coverage/OPEN_API_ACCOUNTS_CREATE_FREE_FORM_TRANSACTION_REQUEST/lcov.info"
  env.AWS_DEFAULT_REGION='us-east-1'
  /*
  */    
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("_srv_jenkins_pec")
    env.tag="true"
    env.ENV='prod'
    env.COST_CENTER='arch_open_api'
    env.STACK_NAME='ARCH-OPEN-API-ACCOUNTS'
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" || env.BRANCH_NAME == "test-flow" )
  {
       
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA")
    env.ENV='comdev'
    env.COST_CENTER='arch_open_api_dev'
    env.STACK_NAME='ARCH-OPEN-API-ACCOUNTS'
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA")
    env.ENV='compredev'
    env.COST_CENTER='arch_open_api_dev'
    env.STACK_NAME='ARCH-OPEN-API-ACCOUNTS'
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA")
    env.tag="true"
    env.ENV='qa'
    env.COST_CENTER='arch_open_api_dev'
    env.STACK_NAME='ARCH-OPEN-API-ACCOUNTS'
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    sh "abort"
  }
}
return this;
