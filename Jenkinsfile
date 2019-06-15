node ("master")
{
  stage('Checkout SCM')
  {
    checkout scm
  }
  stage ("load framework.groovy")
  {
    branch = "master"
    url_git = "https://github.com/matiasgonzalocalvo/FrameworkJenkins"
    env.folder = "devops"
    sh "mkdir -p ${folder}"
    dir ("${folder}")
    {
      git(
        url: "${url_git}",
        branch: "${branch}"
      )
    }
    devops = load "devops/framework.groovy"
    devops.main()
  }
}

