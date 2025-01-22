# Deployment

We use containers to run our system. This allows us to provide a consistent environment for our software components regardless of whether they are running on a developers machine locally or on a virtual machine in a data center or in a kubernetes cluster at a public cloud provider.

Our containers can be build by using the Dockerfiles in the repository:

### KST Platform:

- Backend: [Dockerfile](https://github.com/Fraunhofer-IESE/KickStartTrustee/blob/main/src/kst-platform/server/Dockerfile)
- Frontend: [Dockerfile](https://github.com/Fraunhofer-IESE/KickStartTrustee/blob/main/src/kst-platform/ui/Dockerfile)

### KST Basic FMIS:

- [Dockerfile](https://github.com/Fraunhofer-IESE/KickStartTrustee/blob/example-farming-trustee/src/example/kst-basic-fmis/Dockerfile)
