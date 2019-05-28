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
        env.COST_CENTER='comercios_dev'
        env.STACK_NAME='AuthorizerComafiApi'
        env.FILES_BUCKET='AuthorizerComafiApi'
    }
    else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA")
        env.ENV='qa'
        env.COST_CENTER='comercios_dev'
        env.STACK_NAME='AuthorizerComafiApi'
        env.FILES_BUCKET='AuthorizerComafiApi'
    }
    else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.ENV='dev'
        env.COST_CENTER='comercios_dev'
        env.STACK_NAME='AuthorizerComafiApi'
        env.FILES_BUCKET='AuthorizerComafiApi'
    }
    else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*" || env.BRANCH_NAME == "test-flow")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA")
        env.ENV='predev'
        env.COST_CENTER='comercios_dev'
        env.STACK_NAME='AuthorizerComafiApi'
        env.FILES_BUCKET='AuthorizerComafiApi'
    }
    else if (env.BRANCH_NAME == "test-flow")
    {
        sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
        devops.aws_config("AWS_DESA_CMF")
        env.ENV='predev'
        env.COST_CENTER='comercios_dev'
        env.STACK_NAME='AuthorizerComafiApi'
        env.FILES_BUCKET='AuthorizerComafiApi'
    }
    else
    {
        echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
        currentBuild.result = 'ABORTED'
        error('Branch no manejado')
        sh "abort"
    }
}
return this;
