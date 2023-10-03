const Discord = require('discord.js');
const { prefix, token } = require('./config.json')
const fs = require('fs');
const speech = require('@google-cloud/speech')
const spawn = require("child_process").spawn
const { Transform } = require('stream');

const client = new Discord.Client();
const vclient = new speech.SpeechClient();

const request = {
    config: {
        encoding: 'LINEAR16',
        sampleRateHertz: 48000,
        languageCode: 'en-US',
    },
    interimResults: true,
};



client.once('ready', () => {
    console.log('Ready!');
});
connection = ""
channel = ""

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
    if (message.content === '!listen') {
        if (connection) {
            //send a silent frame to allow for listening
            const SILENCE_FRAME = Buffer.from([0xF8, 0xFF, 0xFE]);

            class Silence extends Transform {
                _read() {
                    this.push(SILENCE_FRAME);
                }
            }
            connection.play(new Silence(), { type: 'opus' });

            for (const [memberID, member] of channel.members) {
                if (member.user.tag != 'Ears#9108') {
                    const audio = await connection.receiver.createStream(member.user, { mode: "pcm", end: "manual" });
                    const recognizeStream = vclient
                        .streamingRecognize(request)
                        .on('error', console.error)
                        .on('data', data =>
                            process.stdout.write(
                                data.results[0] && data.results[0].alternatives[0]
                                    ? member.user.username + `: ${data.results[0].alternatives[0].transcript}\n`
                                    : `\n\nReached transcription time limit, press Ctrl+C\n`
                            )
                        )


                    const editStream = new Transform({
                        writeableObjectMode: true,
                        transform(chunk, encoding, callback) {
                            var data = Float32Array.from(chunk);
                            var newData = [];
                            var silence = true;
                            for (i = 0; i < data.length; i = i + 4) {
                                newData.push((data[i] + data[i + 2]) / 2)
                                newData.push((data[i + 1] + data[i + 3]) / 2)
                                if (data[i] > 0) {
                                    silence = false
                                }
                            }
                            if (silence) {
                                newData[1] = 60;
                            }
                            newData = Buffer.from(newData);
                            callback(null, newData);
                        }
                    })
                    if (audio) {
                        console.log(member.user.tag);
                    }

                    audio.on("error", (error) => {
                        console.log(error)
                    });


                    audio.pipe(editStream);


                    editStream.pipe(recognizeStream);


                }

            }

        } else {
            message.channel.send("Not in a voice channel")
        }
    };
    if (message.content === '!stop') {
        if (connection) {
            for (const [memberID, member] of channel.members) {
                if (member.user.tag != 'Ears#9108') {
                    const audio = await connection.receiver.createStream(member.user, { mode: "pcm", end: "manual" });
                    audio.destroy()
                    editstream.destroy()
                    recognizeStream.destroy()
                }
            }
            connection.disconnect()

        } else {
            message.channel.send("Not in a voice channel")
        }
    }
    if (message.content === '!shutdown') {
        client.destroy()
    }
});

client.login(token);
