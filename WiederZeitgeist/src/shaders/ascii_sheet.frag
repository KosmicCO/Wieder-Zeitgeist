#version 330

in vec2 TexCoords;

out vec4 FragColor;

uniform vec4 foreColor;
uniform vec4 backColor;
uniform sampler2D tex;

void main() {
    FragColor = foreColor * (1 - texture(tex, TexCoords)) + backColor * (texture(tex, TexCoords));
}