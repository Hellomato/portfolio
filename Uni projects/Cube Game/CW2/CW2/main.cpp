#include "GL/glew.h"
#include "GL/freeglut.h"
#include "GL/glut.h"
#include "GLFW/glfw3.h"
#include "LoadShaders.h"
#include <glm/glm.hpp> //includes GML
#include <glm/ext/matrix_transform.hpp> // GLM: translate, rotate
#include <glm/ext/matrix_clip_space.hpp> // GLM: perspective and ortho 
#include <glm/gtc/type_ptr.hpp> // GLM: access to the value_ptr
#include <iostream>
#include <vector>
#include<fstream>
#include<conio.h>
#include<string>
#define STB_IMAGE_IMPLEMENTATION
#include <regex>
#include "stb_image.h"

std::vector<GLuint> VAOs;
std::vector<GLuint> Buffers;
std::vector<glm::vec3>positions;
std::vector<glm::vec3>scales;
std::vector<glm::mat4>mvs;
bool gravity;
GLuint texture1;
GLuint shader;
GLuint noTextureShader;
int vertexCount;
#define BUFFER_OFFSET(a) ((void*)(a))
int jumptimer = 0;
int jumps = 2;
int inputdelay = 0;

void parse(std::string path, std::vector<glm::vec3>& v, std::vector<glm::vec2>& vt, std::vector<glm::vec3>& vn) {
	std::string line;
	std::ifstream myFile(path);
	std::vector<std::string> lines;
	//load file into vector
	if (myFile.is_open()) {

		while (getline(myFile, line)) {
			lines.push_back(line);
			//std::cout << line << std::endl;

		}
		std::cout << "Loaded, parsing now" << std::endl;
	}

	std::regex vregex("v\\s+(-?[0-9]+\\.?[0-9]*)\\s+(-?[0-9]+\\.?[0-9]*)\\s+(-?[0-9]+\\.?[0-9]*)");
	std::regex vtregex("vt\\s*(-?[0-9]+\\.?[0-9]*)\\s+(-?[0-9]+\\.?[0-9]*)");
	std::regex vnregex("vn\\s*(-?[0-9]\\.?[0-9]*)\\s+(-?[0-9]\\.?[0-9]*)\\s+(-?[0-9]\\.?[0-9]*)");
	std::regex fregex("f\\s*(([0-9]*)\\/([0-9]*)\\/([0-9]*))\\s*(([0-9]*)\\/([0-9]*)\\/([0-9]*))\\s*(([0-9]*)\\/([0-9]*)\\/([0-9]*))\\s*(([0-9]*)\\/([0-9]*)\\/([0-9]*))?");
	std::smatch match;
	std::vector<glm::vec3> tV;
	std::vector<glm::vec2> tvt;
	std::vector<glm::vec3> tvn;
	std::vector<std::vector<int>> faces;

	for (int i = 0; i < lines.size(); i++) {

		if (std::regex_search(lines[i], match, vregex)) {
			glm::vec3 vertex;
			vertex.x = std::stod(match[1]);
			vertex.y = std::stod(match[2]);
			vertex.z = std::stod(match[3]);
			tV.push_back(vertex);
		}
		else if (std::regex_search(lines[i], match, vtregex)) {

			glm::vec2 texture;
			texture.x = std::stod(match[1]);
			texture.y = std::stod(match[2]);
			tvt.push_back(texture);
		}
		else if (std::regex_search(lines[i], match, vnregex)) {

			glm::vec3 normal;
			normal.x = std::stod(match[1]);
			normal.y = std::stod(match[2]);
			normal.z = std::stod(match[3]);
			tvn.push_back(normal);
		}
		else if (std::regex_search(lines[i], match, fregex)) {
			std::vector<int> face;

			for (int j = 2; j < match.size(); j++) {

				if (match[j].matched == true) {
					const float value = std::stod(match[j]);
					face.push_back(value - 1);
				}
				if (j % 4 == 0) {
					j = j + 1;
				}
			}
			faces.push_back(face);
		}
	}


	for (int i = 0; i < faces.size(); i++) {
		if (faces[i].size() == 12) {
			v.push_back(tV[faces[i][0]]);
			v.push_back(tV[faces[i][3]]);
			v.push_back(tV[faces[i][6]]);

			v.push_back(tV[faces[i][6]]);
			v.push_back(tV[faces[i][9]]);
			v.push_back(tV[faces[i][0]]);

			vt.push_back(tvt[faces[i][1]]);
			vt.push_back(tvt[faces[i][4]]);
			vt.push_back(tvt[faces[i][7]]);

			vt.push_back(tvt[faces[i][7]]);
			vt.push_back(tvt[faces[i][10]]);
			vt.push_back(tvt[faces[i][1]]);

			vn.push_back(tvn[faces[i][2]]);
			vn.push_back(tvn[faces[i][5]]);
			vn.push_back(tvn[faces[i][8]]);

			vn.push_back(tvn[faces[i][8]]);
			vn.push_back(tvn[faces[i][11]]);
			vn.push_back(tvn[faces[i][2]]);

		}
		else {
			v.push_back(tV[faces[i][0]]);
			v.push_back(tV[faces[i][3]]);
			v.push_back(tV[faces[i][6]]);

			vt.push_back(tvt[faces[i][1]]);
			vt.push_back(tvt[faces[i][4]]);
			vt.push_back(tvt[faces[i][7]]);

			vn.push_back(tvn[faces[i][2]]);
			vn.push_back(tvn[faces[i][5]]);
			vn.push_back(tvn[faces[i][8]]);

		}
	}


	vertexCount = v.size();
}

void loadTexture(GLuint& texture, std::string texturepath)
{
	// load and create a texture 
	// -------------------------

	// texture 1
	// ---------
	glGenTextures(1, &texture);
	glBindTexture(GL_TEXTURE_2D, texture);
	// set the texture wrapping parameters
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);	// set texture wrapping to GL_REPEAT (default wrapping method)
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	// set texture filtering parameters
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	// load image, create texture and generate mipmaps
	GLint width, height, nrChannels;
	stbi_set_flip_vertically_on_load(true); // tell stb_image.h to flip loaded texture's on the y-axis.
	unsigned char* data = stbi_load(texturepath.c_str(), &width, &height, &nrChannels, 0);
	if (data)
	{
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		glGenerateMipmap(GL_TEXTURE_2D);
	}
	else
	{
		std::cout << "Failed to load texture" << std::endl;
	}
	stbi_image_free(data);
}

float
randomf(const float min, const float max) {

	float num = rand() / (float)RAND_MAX;
	return min + num * (max - min);
}

void
loadfloor() {
	GLuint tvao;
	glGenVertexArrays(1, &tvao);
	VAOs.push_back(tvao);
	glBindVertexArray(VAOs[1]);


	// ambient light
	glm::vec4 ambient = glm::vec4(0.1f, 0.1f, 0.1f, 1.0f);
	//adding the Uniform to the shader
	GLuint aLoc = glGetUniformLocation(shader, "ambient");
	glUniform4fv(aLoc, 1, glm::value_ptr(ambient));

	// light object
	glm::vec3 lightPos = glm::vec3(100.0f, 25.0f, 100.0f);
	GLuint dLightPosLoc = glGetUniformLocation(shader, "lightPos");
	glUniform3fv(dLightPosLoc, 1, glm::value_ptr(lightPos));


	// diffuse light
	glm::vec3 diffuseLight = glm::vec3(0.5f, 0.2f, 0.7f);
	GLuint dLightLoc = glGetUniformLocation(shader, "dLight");
	glUniform3fv(dLightLoc, 1, glm::value_ptr(diffuseLight));

	// specular light
	glm::vec3 specularLight = glm::vec3(0.7f);
	GLfloat shininess = 256; //128 is max value
	GLuint sLightLoc = glGetUniformLocation(shader, "sLight");
	GLuint sShineLoc = glGetUniformLocation(shader, "sShine");
	glUniform3fv(sLightLoc, 1, glm::value_ptr(specularLight));
	glUniform1fv(sShineLoc, 1, &shininess);


	static const GLfloat  verticies[] = {
	0.5f,-0.5f,-0.5f, // triangle 1 : begin
	0.5f,-0.5f,0.5f,
	-0.5f,-0.5f,0.5f, // triangle 1 : end

	0.5f,-0.5f,-0.5f, // triangle 2 : begin
	-0.5f,-0.5f,0.5f,
	-0.5f,-0.5f,-0.5f, // triangle 2 : end

	-0.5f,0.5f,-0.5f,
	-0.5f,0.5f,0.5f,
	0.5f,0.5f,0.5f,

	-0.5f,0.5f,-0.5f,
	0.5f,0.5f,0.5f,
	0.5f,0.5f,-0.5f,

	0.5f,-0.5f,-0.5f,
	-0.5f,-0.5f,-0.5f,
	-0.5f,0.5f,-0.5f,

	0.5f,-0.5f,-0.5f,
	-0.5f,0.5f,-0.5f,
	0.5f,0.5f,-0.5f,

	-0.5f,0.5f,0.5f,
	-0.5f,0.5f,-0.5f,
	-0.5f,-0.5f,-0.5f,

	-0.5f,0.5f,0.5f,
	-0.5f,-0.5f,-0.5f,
	-0.5f,-0.5f,0.5f,

	0.5f,-0.5f,0.5f,
	0.5f,-0.5f,-0.5f,
	0.5f,0.5f,-0.5f,

	0.5f,-0.5f,0.5f,
	0.5f,0.5f,-0.5f,
	0.5f,0.5f,0.5f,

	-0.5f,-0.5f,0.5f,
	0.5f,-0.5f,0.5f,
	0.5f,0.5f,0.5f,

	-0.5f,-0.5f,0.5f,
	0.5f,0.5f,0.5f,
	-0.5f,0.5f,0.5f
	};

	GLuint tbuffer;
	glGenBuffers(1, &tbuffer);
	Buffers.push_back(tbuffer);

	glBindBuffer(GL_ARRAY_BUFFER, Buffers[3]);
	glBufferData(GL_ARRAY_BUFFER, 107 * sizeof(GLfloat), &verticies[0], GL_STATIC_DRAW);
	glVertexAttribPointer(0, 3, GL_FLOAT,
		GL_FALSE, 0, BUFFER_OFFSET(0));

	glEnableVertexAttribArray(0);
}

void
init() {
	GLuint tvao;
	glGenVertexArrays(1, &tvao);
	VAOs.push_back(tvao);
	glBindVertexArray(VAOs[0]);

	ShaderInfo shaders[] = {
	{GL_VERTEX_SHADER,"vertexShader.vert"},
	{GL_FRAGMENT_SHADER,"fragShader.frag"},
	{GL_NONE,NULL}
	};
	shader = LoadShaders(shaders);
	glUseProgram(shader);
	ShaderInfo shaders2[] = {
	{GL_VERTEX_SHADER,"vertexShader.vert"},
	{GL_FRAGMENT_SHADER,"fragShader2.frag"},
	{GL_NONE,NULL}
	};
	noTextureShader = LoadShaders(shaders2);


	// ambient light
	glm::vec4 ambient = glm::vec4(0.1f, 0.1f, 0.1f, 1.0f);
	//adding the Uniform to the shader
	GLuint aLoc = glGetUniformLocation(shader, "ambient");
	glUniform4fv(aLoc, 1, glm::value_ptr(ambient));

	// light object
	glm::vec3 lightPos = glm::vec3(100.0f, 25.0f, 100.0f);
	GLuint dLightPosLoc = glGetUniformLocation(shader, "lightPos");
	glUniform3fv(dLightPosLoc, 1, glm::value_ptr(lightPos));


	// diffuse light
	glm::vec3 diffuseLight = glm::vec3(0.5f, 0.2f, 0.7f);
	GLuint dLightLoc = glGetUniformLocation(shader, "dLight");
	glUniform3fv(dLightLoc, 1, glm::value_ptr(diffuseLight));

	// specular light
	glm::vec3 specularLight = glm::vec3(0.7f);
	GLfloat shininess = 256; //128 is max value
	GLuint sLightLoc = glGetUniformLocation(shader, "sLight");
	GLuint sShineLoc = glGetUniformLocation(shader, "sShine");
	glUniform3fv(sLightLoc, 1, glm::value_ptr(specularLight));
	glUniform1fv(sShineLoc, 1, &shininess);
	std::vector<glm::vec3> verticies;
	std::vector<glm::vec2> textures;
	std::vector<glm::vec3> normals;

	parse("media/Creeper-obj/Creeper.obj", verticies, textures, normals);

	for (int i = 0; i < 3; i++) {
		GLuint tbuffer;
		glGenBuffers(1, &tbuffer);
		Buffers.push_back(tbuffer);

	}
	glBindBuffer(GL_ARRAY_BUFFER, Buffers[0]);
	glBufferData(GL_ARRAY_BUFFER, verticies.size() * sizeof(glm::vec3), &verticies[0], GL_STATIC_DRAW);
	glVertexAttribPointer(0, 3, GL_FLOAT,
		GL_FALSE, 0, BUFFER_OFFSET(0));

	glBindBuffer(GL_ARRAY_BUFFER, Buffers[1]);
	glBufferData(GL_ARRAY_BUFFER, textures.size() * sizeof(glm::vec2), &textures[0], GL_STATIC_DRAW);
	glVertexAttribPointer(1, 2, GL_FLOAT,
		GL_FALSE, 0, BUFFER_OFFSET(0));

	loadTexture(texture1, "media/Creeper-obj/Texture.png");
	glUniform1i(glGetUniformLocation(shader, "texture1"), 0);


	glUniform1i(glGetUniformLocation(shader, "texture1"), 0);


	glBindBuffer(GL_ARRAY_BUFFER, Buffers[2]);
	glBufferData(GL_ARRAY_BUFFER, normals.size() * sizeof(glm::vec3), &normals[0], GL_STATIC_DRAW);
	glVertexAttribPointer(2, 3, GL_FLOAT,
		GL_FALSE, 0, BUFFER_OFFSET(0));

	glEnableVertexAttribArray(0);
	glEnableVertexAttribArray(1);
	glEnableVertexAttribArray(2);



}


void updateInput(GLFWwindow* window, glm::vec3& position, glm::vec3& rotation, glm::vec3& scale) {
	float ms = 0.3f;
	glm::vec3 movement(ms * -sin(glm::radians(rotation.y)), ms * -sin(glm::radians(rotation.x)) * cos(glm::radians(rotation.y)), -ms * cos(glm::radians(rotation.x)) * cos(glm::radians(rotation.y)));

	if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
		position -= movement;
	}
	if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
		position += movement;
	}
	if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
		position += glm::normalize(glm::cross(movement, glm::vec3(0.f, 1.f, 0.f))) * ms;
	}
	if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
		position -= glm::normalize(glm::cross(movement, glm::vec3(0.f, 1.f, 0.f))) * ms;
	}
	//if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
	//	position.x += 0.2f;
	//}
	//if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
	//	position.x -= 0.2f;
	//}
	if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS) {
		rotation.y += 2.f;
	}
	if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS) {
		rotation.y -= 2.f;
	} 
	if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS && jumps > 0  && inputdelay == 0) {
		jumps -= 1;
		jumptimer += 30; 
		inputdelay = 40;
	}
	
}

int CheckCollision(glm::vec3 obj1pos) {
	glm::vec3 posmin(obj1pos - glm::vec3(5.f));
	glm::vec3 posMax(obj1pos + glm::vec3(5.f));
	for (int i = 0; i < positions.size(); i++) {
		glm::vec3 pos2min(positions[i] + scales[i] );
		glm::vec3 pos2Max(positions[i] - scales[i] );
		
		bool colX = posmin.x> pos2Max.x - 2.5 && posMax.x < pos2min.x + 2.5;
		bool colY = obj1pos.y + 5 >= positions[i].y + 2.5&& obj1pos.y <= 2.5 + positions[i].y + scales[i].y;
		bool colZ = posmin.z> pos2Max.z - 2.5&& posMax.z < pos2min.z + 2.5;


		if (colX && colY && colZ) {
			gravity = false;
			return i;
		}
	}
	return -1;
}

int
main(int argc, char** argv)
{
	time_t t;
	int score = 0;
	srand((unsigned)time(&t));

	glfwInit();
	int timeFalling = 0;

	GLFWwindow* window = glfwCreateWindow(800, 600, "Model Loader", NULL, NULL);

	glfwMakeContextCurrent(window);
	glewInit();

	init();
	//init matrices
	glm::vec3 position(1.f);
	glm::vec3 rotation(0.f);
	glm::vec3 scale(5.f);

	glm::vec3 platformposition(0.f,0.f,0.f);
	glm::vec3 platformscale(10.f,0.2f,10.f);
	
	positions.push_back(platformposition);
	scales.push_back(platformscale);

	platformposition= glm::vec3(11.f, 5.f, 11.f);
	platformscale = glm::vec3(10.f, 0.1f, 10.f);
	positions.push_back(platformposition);
	scales.push_back(platformscale);
	glm::mat4 model(1.f);
	model = glm::translate(model, position);

	model = glm::scale(model, scale);
	for (int i = 0; i < 3; i++) {
		mvs.push_back(glm::mat4(0.f));
	}

	glm::vec3 cameraPosition = glm::vec3(0.f, 2.f, 5.f);

	glm::vec3 cameraTarget = position;
	glm::vec3 cameraUp = glm::vec3(0, 1, 0);

	glm::mat4 view = glm::mat4(1.0f);
	view = glm::lookAt(cameraPosition, cameraTarget, cameraUp);
	loadfloor();
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);
	glUseProgram(shader);
	while (!glfwWindowShouldClose(window)) {

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		
		int hitIndex = CheckCollision(position);
		if (hitIndex >= 0) {
			jumps = 2;
		}if (hitIndex == positions.size() - 1) {

			platformposition = position + glm::vec3(randomf(-30, 30), randomf(-40, 20), randomf(-30, 30));
			platformscale = glm::vec3(randomf(5,20), 0.1f,randomf(5,20));

			positions.push_back(platformposition);
			scales.push_back(platformscale);

			score += 1;

			if (positions.size() >= 3) {
				positions.erase(positions.begin());
				scales.erase(scales.begin());
			}
		}
		updateInput(window, position, rotation, scale);
		if (gravity) {
			
 			position.y -= 0.5 - 1/exp(timeFalling);
			timeFalling += 1;
		}
		else {
			gravity = true;
			timeFalling = 0;
		}
		if (jumptimer > 0) {
			position.y += 1 - 1 / jumptimer;
			jumptimer -= 1;
		}
		


		//glUseProgram(shader);
		//cameraPosition = position + glm::vec3(0.f, 2.f, 3.f);
		glm::vec3 camRotation(30 * -sin(glm::radians(rotation.y)),30 * -sin(glm::radians(rotation.x)) * cos(glm::radians(rotation.y)) + 20, -30 * cos(glm::radians(rotation.x)) * cos(glm::radians(rotation.y)));
		cameraPosition = position + camRotation;
		cameraTarget = position;

		// creating the view matrix
		view = glm::lookAt(cameraPosition, cameraTarget, cameraUp);
		// creating the projection matrix
		glm::mat4 projection = glm::perspective(glm::radians(90.0f), 4.0f / 3, 0.1f, 100.0f);


		for (int i = 0; i < positions.size(); i++) {
			glBindVertexArray(VAOs[1]);
			model = glm::mat4(1.f);
			model = glm::translate(model, positions[i]);
			model = glm::scale(model, scales[i]);

			glm::mat4 mv = view * model;
			mvs[i] = mv;
			int mvLoc = glGetUniformLocation(shader, "mv_matrix");
			glUniformMatrix4fv(mvLoc, 1, GL_FALSE, glm::value_ptr(mv));
			//adding the Uniform to the shader
			int pLoc = glGetUniformLocation(shader, "p_matrix");
			glUniformMatrix4fv(pLoc, 1, GL_FALSE, glm::value_ptr(projection));

			glDrawArrays(GL_TRIANGLES, 0, 36);
			


		}
		
		glBindVertexArray(VAOs[0]);
		model = glm::mat4(1.f);
		model = glm::translate(model, position);
		model = glm::rotate(model, glm::radians(rotation.x), glm::vec3(1.f, 0.f, 0.f));
		model = glm::rotate(model, glm::radians(rotation.y), glm::vec3(0.f, 1.f, 0.f));
		model = glm::rotate(model, glm::radians(rotation.z), glm::vec3(0.f, 0.f, 1.f));
		model = glm::scale(model, scale);

		glm::mat4 mv = view * model;
		mvs[2] = mv;
		//adding the Uniform to the shader
		int mvLoc = glGetUniformLocation(shader, "mv_matrix");
		glUniformMatrix4fv(mvLoc, 1, GL_FALSE, glm::value_ptr(mv));
		//adding the Uniform to the shader
		int pLoc = glGetUniformLocation(shader, "p_matrix");
		glUniformMatrix4fv(pLoc, 1, GL_FALSE, glm::value_ptr(projection));


		glDrawArrays(GL_TRIANGLES, 0, 72);

		glfwSwapBuffers(window);
		glfwPollEvents();
		if (inputdelay > 0) {
			inputdelay -= 1;
		}
		if (timeFalling > 200) {
			glfwSetWindowShouldClose(window, true);
		}

	}
	glfwDestroyWindow(window);

	glfwTerminate();
	std::cout << "///////////////////////////////////////////////////" << std::endl;
	std::cout << "////////////////You scored: " << score << "/////////////////////"<< std::endl;
	std::cout << "///////////////////////////////////////////////////" << std::endl;
}