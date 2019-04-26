def modules = [:]
pipeline {
    agent any
    stages {
        stage('config docker') {
            steps {
                script {
                    //externalScripts = load 'function.groovy'
                    setenv()
                    //loadScripts()
                    //externalScripts.aws_config()
                    //sh "export"
                    //sh "echo prueba ${prueba}"
                }
            }
        }
        stage(build_list[0]) {
            steps {
                script {
                    //externalScripts.test()
                    sh "export"
                    sh "echo prueba ${prueba}"
                }
            }
        }
    }
    post {
        always {
            script {
                echo "always"
                //send_slack("always",":squirrel:")
            }
        }
        failure {
            script {
                echo "failure"
                //send_slack("failure",":squirrel:")
            }
        }
        unstable {
            script {
                echo "unstable"
                //send_slack("unstable",":squirrel:")
            }
        }
    }
}
def loadScripts() {
//    branch = "master"
//    git(
//        url: 'https://bitbucket.org/comafi/devops-jenkins',
//        credentialsId: 'devops-bitbucket',
//        branch: "${branch}"
//    )
//    sh "ls -altrh "
	externalScripts = load 'function.groovy'
}

def setenv() {
    if (env.BRANCH_NAME == "master") {
        env.prueba="master"
        prueba="master"
        echo "cargue la variable ${env.prueba} - prueba - ${prueba}"
        build_list = ['job1', 'job2', 'job3']
    } else {
        echo "branch else"
    }

}
