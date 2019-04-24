def modules = [:]
pipeline {
    agent {
        label 'jenkins-slave-comafi-nodejsdtk'
	}
    stages {
        stage('config docker') {
            steps {
                script {
                    sh "ls -atlrh"
                    loadScripts()
                    externalScripts.aws_config()
                }
            }
        }
        stage('Test Funciones') {
            steps {
                sh """
                    echo "hola mundo"
                """
            }
        }
    }
    post {
        always {
            echo "always"
            //send_slack("always",":squirrel:")
        }
        failure {
            echo "failure"
            //send_slack("failure",":squirrel:")
        }
        unstable {
            echo "unstable"
            //send_slack("unstable",":squirrel:")
        }
    }
}
def loadScripts() {
    sh "ls -atlrh"
	externalScripts = load 'function.groovy'
}
