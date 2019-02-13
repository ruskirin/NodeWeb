precision mediump float;

varying vec4 v_NodeVertexColor;

//radius to draw circle
uniform float u_NodeRadius;

//sampler contains the data of the texture itself
uniform sampler2D u_NodeSampler;

//takes the texture coords from the vertex shader
varying vec2 v_NodeTexCoords;

void main() {
    //texture2D reads the value of the current pixel in the texture
    gl_FragColor = (v_NodeVertexColor * texture2D(u_NodeSampler, v_NodeTexCoords));
}
