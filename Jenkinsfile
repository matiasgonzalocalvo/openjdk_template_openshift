def modules = [:]
pipeline {
    agent {
        label 'jenkins-slave-comafi-nodejsdtk'
	}
    stages {
        stage('config docker') {
            when {
                anyof {
                    branch 'master'
                }
                //environment {
                    //prueba="prueba"
                //}
            }
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
                    sh "export"
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
