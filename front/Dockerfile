FROM node:15-buster

COPY package.json /opt/pokemon_tool_front/package.json
COPY package.json /opt/pokemon_tool_front/yarn.lock
WORKDIR /opt/pokemon_tool_front
RUN yarn install

COPY . /opt/pokemon_tool_front
CMD yarn serve
