#!/bin/sh

# Link WSL bash_history to bash_history
ln -sf /WSL_USER/.zsh_history ~/.zsh_history

# Install commitizen globally
npm install -g commitizen

# Add configuration to .czrc for commitizen
echo '{"path": "cz-conventional-changelog"}' >> ~/.czrc

export ZSH_CUSTOM=/home/vscode/.oh-my-zsh/custom
export CONFIGURATION_PATH=/workspaces/rtldev-middleware-java-sdk/.devcontainer/configurations
git clone --depth=1 https://github.com/romkatv/powerlevel10k.git $ZSH_CUSTOM/themes/powerlevel10k \
    && chown -R vscode:vscode $ZSH_CUSTOM/themes/powerlevel10k

git clone https://github.com/zsh-users/zsh-autosuggestions $ZSH_CUSTOM/plugins/zsh-autosuggestions \
    && chown -R vscode:vscode $ZSH_CUSTOM/plugins/zsh-autosuggestions

cp $CONFIGURATION_PATH/.p10k.zsh ~/.p10k.zsh

cp $CONFIGURATION_PATH/.zshrc ~/.zshrc

mkdir -p ~/.cache && cp $CONFIGURATION_PATH/p10k-instant-prompt-vscode.zsh ~/.cache/p10k-instant-prompt-vscode.zsh