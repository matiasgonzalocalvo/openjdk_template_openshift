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
                    loadScripts()
                    externalScripts.aws_config()
                }
            }
        }
        stage('Test Funciones') {
            steps {
                script {
                    externalScripts.test()
                }
            }
        }
    }
    post {
        always {
            send_slack("always",":squirrel:")
        }
        failure {
            send_slack("failure",":squirrel:")
        }
        unstable {
            send_slack("unstable",":squirrel:")
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
	externalScripts = load 'function.groovy'
}
