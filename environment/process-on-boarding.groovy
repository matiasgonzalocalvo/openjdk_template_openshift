def setenv()
{
    /*
      Variables Globales a todos los ambientes
    */
    env.sonar_projectKey="process-on-boarding"
    env.sonar_exclusions=""
    env.sonar_javascript_lcov_reportPaths=""
    env.AWS_DEFAULT_REGION='us-east-1'
    /*
    */    
    if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("_srv_jenkins_pec")
        env.ENV='prod'
        env.COST_CENTER='onboarding_prod'
        env.FILES_BUCKET='circuitcreator-deploybucket-comafi'
        env.STACK_NAME='CircuitCreator'
        env.STAGE="prod"
        env.STAGE_BUILD="Build ${STAGE} - ${ENV}"
        env.STAGE_DEPLOY="DEPLOY ${STAGE} - ${ENV}"

    }
    else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA")
        env.ENV='dev'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-circuitcreator-files'
        env.STACK_NAME='CircuitCreator'
        env.STAGE="dev"
        env.STAGE_BUILD="Build ${STAGE} - ${ENV}"
        env.STAGE_DEPLOY="DEPLOY ${STAGE} - ${ENV}"

    }
    else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA")
        env.ENV='predev'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-circuitcreator-files'
        env.STACK_NAME='CircuitCreator'
        env.STAGE="predev"
        env.STAGE_BUILD="Build ${STAGE} - ${ENV}"
        env.STAGE_DEPLOY="DEPLOY ${STAGE} - ${ENV}"
    }
    else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA")
        env.ENV='qa'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-circuitcreator-files'
        env.STACK_NAME='CircuitCreator'
        env.STAGE="qa"
        env.STAGE_BUILD="Build ${STAGE} - ${ENV}"
        env.STAGE_DEPLOY="DEPLOY ${STAGE} - ${ENV}"

    }
    else if (env.BRANCH_NAME == "test-flow")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.ENV='dev'
        env.COST_CENTER='comercios_dev'
        env.FILES_BUCKET='cmf-circuitcreator-files'
        env.STACK_NAME='CircuitCreator'
        env.STAGE="dev"
        env.STAGE_BUILD="Build ${STAGE} - ${ENV}"
        env.STAGE_DEPLOY="DEPLOY ${STAGE} - ${ENV}"
    }
    else
    {
        devops.aws_config("705437fe-118c-4fbc-af26-595cbdc1e752")
        echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
        currentBuild.result = 'ABORTED'
        error('Branch no manejado')
        sh "abort"
    }
}
return this;
