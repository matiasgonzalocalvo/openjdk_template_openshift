node ()
{
  stage ("Checkout devops framework")
  {
    env.JenkinsVersion="1.0"
    /* Variable reponame la cargo con el nombre del repo. despues voy a cargar el archivo environment con este nombre */
    env.repoName = checkout(scm).GIT_URL.tokenize('/').last().split("\\.")[0]
    /* f_funtions es la libreria general que vamos a utilizar*/
    f_funtions="function.groovy"
    f_environment="environment/${repoName}.groovy"
    /* Variales del repo devops*/
    branch = "${BRANCH_NAME}"
    url_git = "https://bitbucket.org/comafi/devops-jenkins"
    credentialsId = "devops-bitbucket"
    folder = "devops"
    /**/
    sh "mkdir -p ${folder}"
    dir ("${folder}")
    {
      git(
        url: "${url_git}",
        credentialsId: "${credentialsId}",
        branch: "${branch}"
      )
      devops = load "${f_funtions}"
      loadvar = load "${f_environment}"
      /* Cargo las variables Globales en la funcion set_env_global debe exister la variable env.f_flujo que es el flujo que se va a usar */
      loadvar.set_env_global()
      /* Cargo el flujo */
      flujo = load "flujos/${f_flujo}"
    }
  }
  stage ("flow")
  {
    /* Ejecuto el flujo */ 
    flujo.flujo()
  }
}
