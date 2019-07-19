def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.f_flujo="devops-flujo.groovy"
  //env.node_docker="arch-jenkins-slave-front"
  env.node_docker="master"
  env.AWS_DEFAULT_REGION='us-east-1'
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    return false
  }
  else if (env.BRANCH_NAME =~ "release/*" || env.BRANCH_NAME =~ "hotfix/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    return true
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    return false
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "bugfix/*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    return false
  }
  else
  {
    echo "ERROR: No entro a ninguna condicion de branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
