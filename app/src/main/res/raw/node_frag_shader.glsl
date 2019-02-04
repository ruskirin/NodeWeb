precision mediump float;

//sampler contains the data of the texture itself
uniform sampler2D u_NodeSampler;

//takes the texture coords from the vertex shader
varying vec2 v_TexCoordsOut;

varying vec4 v_NodeVertexColor;

void main() {
    //texture2D reads the value of the current pixel in the texture
    gl_FragColor = (v_Color * texture2D(u_NodeSampler, v_TexCoordsOut));
}
