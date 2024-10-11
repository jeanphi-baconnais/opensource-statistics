# Opensource-statistiques 📊

🚧 This project is in progress ... 

One objective of this project is to highlight Zenika members contributions. 

To do this, the zenika-open-source GitHub Organization is scanned and data could be saved in a Database.
Datas could be used to find / list all projects maintained by Zenika Members, all projects which Zenika members contributed, etc.

## 🗄️ Tech 

Using :
- [Quarkus](https://quarkus.io/)
- [Cloud SQL](https://quarkus.io/guides/deploying-to-google-cloud#using-cloud-sql) to save data
- [GitHub actions](https://github.com/features/actions) as CICD.

## 🌐 API 

Some resources are available but only the first is in progress : 
- `/github/` to get information about Zenika Open Source GitHub organization and members from GitHub
- `/members/` to get information from GCP database 🚧 not implemented
- `/contributions/` to get information about contributions from GCP database 🚧 not implemented

one future step concerns GitLab. We could easily imagine an endpoint `/gitlab`to get all information in this platform.

## 📝 Setup 

- You need a GitHub token you can generate on [this page](https://github.com/settings/tokens).
- Create a `.env` file based on the `.env-example` file and set the token previously created. 
- Run the application with `quarkus dev` if you have the [Quarkus CLI](https://quarkus.io/guides/cli-tooling) installed on your environment, or `mvn quarkus:dev`command.

## ✨Contribute 

Anyone can contribute to this project. For the moment, please add your question or purpose something in [a new issue](https://github.com/zenika-open-source/opensource-statistics/issues).

![with love by zenika](https://img.shields.io/badge/With%20%E2%9D%A4%EF%B8%8F%20by-Zenika-b51432.svg?link=https://oss.zenika.com)
