# Development image for this repository.
#
# Deliberately thin. The zsh configuration (prompt, autosuggestions) and
# commitizen setup that used to be cloned and copied in from post-create.sh are
# now in the devbase Feature:
# https://github.com/centralnicgroup-opensource/rtldev-middleware-devcontainer-features
#
# The JDK and Maven come from the java devcontainer Feature, declared in
# devcontainer.json, so this file does not pin a language image either.

FROM mcr.microsoft.com/devcontainers/base:2-ubuntu-24.04

# No ENV DEBIAN_FRONTEND here on purpose: set at image level it persists into the
# running container, so an interactive `apt` in a shell session also loses its
# frontend. A layer that needs it sets it for itself.
ENV TERM=xterm-256color
