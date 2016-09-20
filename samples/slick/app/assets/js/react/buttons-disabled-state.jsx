const {ButtonToolbar, Button} = ReactBootstrap;

export const buttonsInstance = (
    <ButtonToolbar>
        <Button bsStyle='primary' bsSize='large' disabled>Primary button</Button>
        <Button bsSize='large' disabled>Button</Button>
    </ButtonToolbar>
);

React.render(buttonsInstance, document.getElementById("app"));