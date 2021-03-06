#version 410 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

in vec4 in_Position;
in vec4 in_Color;
in vec2 in_TextureCoord;

out vec4 pass_Color;
out vec2 pass_TextureCoord;

void main() {
	// gl_Position = in_Position;
	// Override gl_Position with our new calculated position
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;
        gl_Position.x = gl_Position.x/10;
        gl_Position.y = gl_Position.y/10;

	pass_Color = in_Color;
	pass_TextureCoord = in_TextureCoord;
}