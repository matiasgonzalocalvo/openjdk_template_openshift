node ("master")
{
  stage('Checkout SCM')
  {
    checkout scm
  }
  stage ("load framework.groovy")
  {
    branch = "master"
    //url_git = "https://github.com/matiasgonzalocalvo/FrameworkJenkins"
    url_git = "https://github.com/matiasgonzalocalvo/testjenkinsframework"
    env.folder = "devops"
    framework = "framework.groovy"
    sh "mkdir -p ${folder}"
    dir ("${folder}")
    {
      git(
        url: "${url_git}",
        branch: "${branch}"
      )
    }
    devops = load "${folder}/${framework}"
    devops.main()
  }
}

