node ("master")
{
  stage('Checkout SCM')
  {
    checkout scm
  }
  stage ("load framework.groovy")
  {
    branch = "master"
    url_git = "https://github.com/matiasgonzalocalvo/testjenkinsframework"
    credentialsId = "matiasgonzalocalvo_github"
    env.folder = "devops"
    sh "mkdir -p devops"
    dir ("${folder}")
    {
      git(
        url: "${url_git}",
        credentialsId: "${credentialsId}",
        branch: "${branch}"
      )
    }
    devops = load "devops/framework.groovy"
    devops.main()
  }
}

