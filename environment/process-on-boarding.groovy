def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="ARCH-CircuitsCreator"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
  env.STAGE_BUILD="Build ${STAGE} - ${ENV}"
  env.STAGE_DEPLOY="DEPLOY ${STAGE} - ${ENV}"  
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='onboarding_prod'
    env.FILES_BUCKET='circuitcreator-deploybucket-comafi'
    env.STAGE="prod"
    if ( cuenta == "_srv_jenkins_pec" )
    {
      devops.aws_config("_srv_jenkins_pec")
      env.ENV='prod'
      env.STACK_NAME='CircuitCreator'
      return true
    }
    else
    {
      return false
    }
  }
  else if (env.BRANCH_NAME =~ "release/*"  || env.BRANCH_NAME =~ "hotfix/*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comercios_dev'
    env.FILES_BUCKET='cmf-circuitcreator-files'
    env.STAGE="qa"
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      env.ENV='qa'
      devops.aws_config("AWS_DESA_CMF")
      env.STACK_NAME='CircuitCreator'      
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      env.ENV='qa'
      devops.aws_config("AWS_DESA")
      env.STACK_NAME='CIRCUIT-CREATOR'
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
    env.FILES_BUCKET='cmf-circuitcreator-files'
    env.STAGE="dev"
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      env.ENV='dev'
      devops.aws_config("AWS_DESA_CMF")
      env.STACK_NAME='CircuitCreator'
      env.DEPLOY_BUCKETNAME='tesla-prius-new-deploy'
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      env.ENV='dev'
      devops.aws_config("AWS_DESA")
      env.DEPLOY_BUCKETNAME='tesla-prius-deploy'
      env.STACK_NAME='CIRCUIT-CREATOR-v2'
      return true
    }
    else
    {
      return false
    }
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*" || env.BRANCH_NAME =~ "bugfix/*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.COST_CENTER='comercios_dev'
    env.FILES_BUCKET='cmf-circuitcreator-files'
    env.STAGE="predev"
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      env.ENV='predev'
      devops.aws_config("AWS_DESA_CMF")
      env.DEPLOY_BUCKETNAME='tesla-prius-new-deploy'
      env.STACK_NAME='CircuitCreator'
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      env.ENV='predev'
      devops.aws_config("AWS_DESA")
      env.DEPLOY_BUCKETNAME='tesla-prius-deploy'
      env.STACK_NAME='CIRCUIT-CREATOR-v2'
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
    env.FILES_BUCKET='cmf-circuitcreator-files'
    env.STAGE="dev"
    if ( cuenta == "null" || cuenta == "AWS_DESA_CMF" )
    {
      env.ENV='dev'
      devops.aws_config("AWS_DESA_CMF")
      env.STACK_NAME='CircuitCreator'
      return true
    }
    else if ( cuenta == "AWS_DESA" )
    {
      env.ENV='dtkdev'
      devops.aws_config("AWS_DESA")
      env.STACK_NAME='CIRCUIT-CREATOR'
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
    ///error('Branch no manejado')
    //sh "abort"
    devops.fail()
  }
}
return this;
