def component = [
		seboard: true,
		nginx: true
]

pipeline {
	agent any
	stages {
		stage("Checkout") {
			steps {
				checkout scm
			}
		}
		stage("Build") {
			steps {
                script {
					component.each{ entry ->
						stage ("${entry.key} Build"){
							if(entry.value){
								var = entry.key
								sh "cd ${entry.key}"
								sh "docker build -t maanjong/se-dev:${entry.key} ."
								sh "cd .."
							}	
						}
					}
				}
			}
		}
		stage("Tag and Push") {
			steps {
                script {
					component.each{ entry ->
						stage ("${entry.key} Push"){
							if(entry.value){
								var = entry.key
								withCredentials([[$class: 'UsernamePasswordMultiBinding',
								credentialsId: 'maanjong-docker-hub',
								usernameVariable: 'DOCKER_USER_ID',
								passwordVariable: 'DOCKER_USER_PASSWORD'
								]]){
								sh "docker login -u ${DOCKER_USER_ID} -p ${DOCKER_USER_PASSWORD}"
								sh "docker push maanjong/se-dev:${entry.key}"
								}
							}
						}
					}
				}
			}	
		}
		stage("Execute") {
            steps {
                sh "cd seboard"
                sh "docker compose up -d"
            }
        }
	}

}
