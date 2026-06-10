pipeline {

    agent any


    /*
    Déclaration des paramètres.
    Ils apparaîtront dans Jenkins avec l'option :
    "Build with Parameters"
    */

    parameters {

        string(
            name: 'NAME',
            defaultValue: 'DevOps User',
            description: 'Please tell me your name'
        )

        text(
            name: 'DESC',
            defaultValue: 'Pipeline CI/CD Jenkins GitHub + Snyk',
            description: 'Description du Job'
        )

        booleanParam(
            name: 'SKIP_TEST',
            defaultValue: false,
            description: 'Skip running Tests ?'
        )

        choice(
            name: 'BRANCH',
            choices: [
                'main',
                'dev',
                'test'
            ],
            description: 'Choose Git branch'
        )

        booleanParam(
            name: 'SKIP_SNYK',
            defaultValue: false,
            description: 'Skip Snyk security scans ?'
        )

    }


    environment {

        APP_NAME    = "web-app"
        DOCKER_IMAGE = "web-app"
        // Credential "snyk-token" à créer dans Jenkins > Manage Jenkins > Credentials
        SNYK_TOKEN  = credentials('snyk-token')

    }


    stages {


        // ─────────────────────────────────────────────────────────────
        stage('01 - PRINT PARAMETERS') {

            steps {

                echo "Hello ${params.NAME}"

                echo """
                Job Description : ${params.DESC}
                Branch Selected : ${params.BRANCH}
                Skip Test       : ${params.SKIP_TEST}
                Skip Snyk       : ${params.SKIP_SNYK}
                """

            }

        }


        // ─────────────────────────────────────────────────────────────
        stage('02 - CHECKOUT GITHUB') {

            steps {

                echo "Downloading source code from branch: ${params.BRANCH}"

                git branch: "${params.BRANCH}",
                    url: 'https://github.com/Nayati-Matrat/devsecops-pipeline.git'

            }

        }


        // ─────────────────────────────────────────────────────────────
        stage('03 - BUILD APPLICATION') {

            steps {

                echo "Building application with Maven..."

                sh 'mvn clean package -DskipTests'

            }

        }


        // ─────────────────────────────────────────────────────────────
        stage('04 - RUN TESTS') {

            when {
                expression { return params.SKIP_TEST == false }
            }

            steps {

                echo "Running unit tests..."

                sh 'mvn test'

            }

            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }

        }


        // ─────────────────────────────────────────────────────────────
        stage('05 - SNYK DEPENDENCY SCAN') {

            when {
                expression { return params.SKIP_SNYK == false }
            }

            steps {

                echo "Scanning Maven dependencies with Snyk..."

                sh '''
                    snyk auth $SNYK_TOKEN
                    snyk test --severity-threshold=high --all-projects || true
                '''

            }

        }


        // ─────────────────────────────────────────────────────────────
        stage('06 - DOCKER BUILD') {

            steps {

                echo "Building Docker image: ${DOCKER_IMAGE}:latest"

                sh "docker build -t ${DOCKER_IMAGE}:latest ."

            }

        }


        // ─────────────────────────────────────────────────────────────
        stage('07 - SNYK CONTAINER SCAN') {

            when {
                expression { return params.SKIP_SNYK == false }
            }

            steps {

                echo "Scanning Docker image with Snyk..."

                sh '''
                    snyk container test web-app:latest \
                        --severity-threshold=high \
                        --file=Dockerfile || true
                '''

            }

        }


        // ─────────────────────────────────────────────────────────────
        stage('08 - DEPLOY') {

            steps {

                echo "Deploying application..."

                sh """
                    docker stop ${APP_NAME} || true
                    docker rm   ${APP_NAME} || true

                    docker run -d \
                        --name ${APP_NAME} \
                        -p 8080:8080 \
                        ${DOCKER_IMAGE}:latest
                """

                echo "Application available at http://localhost:8080"

            }

        }


    }


    // ─────────────────────────────────────────────────────────────────
    post {

        success {
            echo """
            ==============================================
            PIPELINE SUCCESS
            Application deployed on http://localhost:8080
            ==============================================
            """
        }

        failure {
            echo """
            ==============================================
            PIPELINE FAILED — please check the logs above
            ==============================================
            """
        }

    }

}
