def modules = [:]
pipeline {
    agent {
        label 'jenkins-slave-comafi-nodejsdtk'
	}
    stages {
        stage('config docker') {
            steps {
                script {
                    //externalScripts = load 'function.groovy'
                    setenv()
                    loadScripts()
                    externalScripts.aws_config()
                    sh "export"
                    sh "echo prueba ${prueba}"
                }
            }
        }
        stage('Test Funciones') {
            steps {
                script {
                    externalScripts.test()
                    sh "export"
                    sh "echo prueba ${prueba}"
                }
            }
        }
    }
    post {
        always {
            script {
                send_slack("always",":squirrel:")
            }
        }
        failure {
            script {
                send_slack("failure",":squirrel:")
            }
        }
        unstable {
            script {
                send_slack("unstable",":squirrel:")
            }
        }
    }
}
def loadScripts() {
    branch = "master"
    git(
        url: 'https://bitbucket.org/comafi/devops-jenkins',
        credentialsId: 'devops-bitbucket',
        branch: "${branch}"
    )
    sh "ls -altrh "
	externalScripts = load 'function.groovy'
}

def setenv() {
    if (env.BRANCH_NAME == "master") {
        environment {
            env.prueba="master"
        }
    } else {
        environment {
            env.prueba="otro"
        }
    }

}
