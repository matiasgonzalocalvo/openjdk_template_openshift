def set_env_global()
{
  /*
    Variables Globales a todos los ambientes
  */
  env.sonar_projectKey="comafi-hbi-front"
  env.sonar_exclusions=""
  env.sonar_javascript_lcov_reportPaths=""
  env.AWS_DEFAULT_REGION='us-east-1'
  env.BUCKET_ID="hbi-front-comafi"
}
def setenv(def cuenta="null")
{
  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "prod")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENV="prod"
    devops.aws_config("AWS_Alternative")
  }
  else if (env.BRANCH_NAME == "impleqa" || env.BRANCH_NAME == "qa" || env.BRANCH_NAME =~ "release/*" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    echo "echo probando parameters  "
    //parameters { choice(name: 'ENV', choices: ['qa', 'qa2', 'glqa'], description: 'ELEGI EL AMBIENTE ') }
    //parameters { choice(name: 'CHOICES', choices: ['one', 'two', 'three'], description: '') }
    def deployOptions = 'qa\nqa2\nglqa'
    def ENV = input(
      id: 'ENV', 
      message: 'En que ambiente deployo????', 
      parameters: 
      [
        [
          $class: 'ChoiceParameterDefinition', 
          choices: deployOptions, 
          description: 'Eleccion de ambiete QA!!', 
          name: 'ambiente'
        ]
      ]
    )
    env.ENV="${ENV}"
    //sh "export"
    echo "you selected: ${ENV}"
    devops.aws_config("AWS_DESA")
  }
  else if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "developjenkinsfile" )
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    env.ENV="dev"
    devops.aws_config("AWS_DESA")
  }
  else if (env.BRANCH_NAME =~ "feature/*" || env.BRANCH_NAME =~ "PR*")
  {
    sh 'echo "$(date) : Seteando variables - BRANCH = ${BRANCH_NAME}"'
    devops.aws_config("AWS_DESA")
    env.ENV="sandbox"
    //set_npm_nexus()
  }
  else
  {
    echo "problema no entro a ninguna condicion branch = ${env.BRANCH_NAME}"
    devops.fail()
  }
}
return this;
