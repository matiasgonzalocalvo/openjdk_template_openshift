def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="process-on-boarding"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
}
def setenv()
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comercios_dev'
    env.FILES_BUCKET='AuthorizerComafiApi'
    env.tag="true"
    if ( cuenta == "_srv_jenkins_pec" )
    {
      env.ENV='prod'
      devops.aws_config("_srv_jenkins_pec")
      env.STACK_NAME='AuthorizerComafiApi'
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
    env.FILES_BUCKET='AuthorizerComafiApi'
    env.tag="true"
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      env.ENV='qa'
      devops.aws_config("AWS_DESA_CMF")
      env.STACK_NAME='AuthorizerComafiApi'
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      env.ENV='dtkqa'
      devops.aws_config("AWS_DESA")
      env.STACK_NAME='AUTHORIZER-COMAFI-API'
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
    env.COST_CENTER='comercios_dev'
    env.FILES_BUCKET='AuthorizerComafiApi'
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      env.ENV='dev'
      devops.aws_config("AWS_DESA_CMF")
      env.STACK_NAME='AuthorizerComafiApi'
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      env.ENV='dtkdev'
      devops.aws_config("AWS_DESA")
      env.STACK_NAME='AUTHORIZER-COMAFI-API'
      return true
    }
    else
    {
      return false
    }
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comercios_dev'
    env.FILES_BUCKET='AuthorizerComafiApi'
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      env.ENV='predev'
      devops.aws_config("AWS_DESA_CMF")
      env.STACK_NAME='AuthorizerComafiApi'
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      env.ENV='dtkpredev'
      devops.aws_config("AWS_DESA")
      env.STACK_NAME='AUTHORIZER-COMAFI-API'
      return true
    }
    else
    {
      return false
    }
  }
  else if (env.BRANCH_NAME == "test-flow")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comercios_dev'
    env.FILES_BUCKET='AuthorizerComafiApi'
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      env.ENV='dev'
      devops.aws_config("AWS_DESA_CMF")
      env.STACK_NAME='AuthorizerComafiApi'
      return true
    } 
    else if ( cuenta == "AWS_DESA" )
    {
      env.ENV='dtkdev'
      devops.aws_config("AWS_DESA")
      env.STACK_NAME='AUTHORIZER-COMAFI-API'
      return true
    } 
    else
    {
      echo "return false"
      return false
    }
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    //currentBuild.result = 'ABORTED'
    //error('Branch no manejado')
    //sh "abort"
    devops.fail()
  }
}
return this;
