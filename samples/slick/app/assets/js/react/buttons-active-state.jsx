const {ButtonToolbar, Button} = ReactBootstrap;

export const buttonsInstance = (
    <ButtonToolbar>
        <Button bsStyle='primary' bsSize='large' active>Primary button</Button>
        <Button bsSize='large' active>Button</Button>
    </ButtonToolbar>
);

React.render(buttonsInstance, document.getElementById("app"));