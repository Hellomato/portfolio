const Discord = require('discord.js');
const { prefix, token } = require('./config.json')
const client = new Discord.Client();


client.once('ready', () => {
    console.log('Ready!');
});

client.on('message', async message => {
    // Join the same voice channel of the author of the message

    if (message.content === '!join') {
        if (message.member.voice.channel) {
            connection = await message.member.voice.channel.join();
            channel = message.member.voice.channel;
        } else {
            message.channel.send("Can't join that channel")
        }
    }
});
client.on("guildMemberSpeaking", function (member, speaking) {
    console.log(`a guild member starts/stops speaking: ${member.tag}`);
});

client.login(token);